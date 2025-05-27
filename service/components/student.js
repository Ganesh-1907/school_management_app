import { db } from "../configs/firebase-config.js";

export const addStudent = async (userData) => {
    console.log(userData, 'userdata');
    try {
        const userDocRef = db.collection("users").doc();
        const userId = userDocRef.id;

        await userDocRef.set({
            name: userData.name,
            role: 'Student',
            password: userData.phone, // Ideally hash this
            class: userData.class,
            phone: userData.phone,
            address: userData.address,
            dateOfBirth: userData.dateOfBirth,
            gender: userData.gender,
            motherName: userData.motherName,
            fatherName: userData.fatherName,
            joiningDate: new Date().toISOString(),
        });
        await db.collection('users-school').doc(userId).set({
            schoolId: userData.schoolId,
            userId: userId,
            role: 'Student',
            joinedAt: new Date().toISOString()
        });

        console.log(`User ${userData.name} created successfully!`);
        return `User ${userData.name} created successfully!`;
    } catch (error) {
        console.error("Error creating user:", error);
        return `Error creating user: ${error.message}`;
    }
};


export const getStudents = async (schoolId) => {
    try {
        const snapshot = await db.collection("users").where("role", "==", "Student").get();
        const studentPromises = snapshot.docs.map(async (doc) => {
            const schoolData = await db.collection("users-school").where("userId", "==", doc.id).get();
            if (schoolData.docs[0]?.data().schoolId === schoolId) {
                return { id: doc.id, ...doc.data() };
            }
            return null;
        });
        const studentsResults = await Promise.all(studentPromises);
        const students = studentsResults.filter(student => student !== null);
        if (students.length === 0) {
            return [];
        } else {
            return students;
        }
    } catch (error) {
        console.error("Error fetching students:", error);
        return [];
    }
};

export const getStudentsHealth = async (schoolId) => {
    try {
        const snapshot = await db.collection("students-health").where("schoolId", "==", schoolId).get();
        const studentsHealth = [];
        const userIdSet = new Set();

        snapshot.forEach((doc) => {
            const data = doc.data();
            studentsHealth.push({ id: doc.id, ...data });
            if (data.studentId) {
                userIdSet.add(data.studentId);
            }
        });

        const userMap = {};
        await Promise.all(Array.from(userIdSet).map(async (userId) => {
            const userDoc = await db.collection("users").doc(userId).get();
            if (userDoc.exists) {
                console.log("userDoc", userDoc.data())
                if(!userDoc.data().role || userDoc.data().role !== "Student") {
                    console.warn("User is not a student:", userId);
                    return;
                }
                const userData = userDoc.data();
                userMap[userId] = userData.name || "Unknown";
            } else {
                return console.warn(`User with ID ${userId} not found.`);
            }
        }));

        const enrichedData = studentsHealth.map((entry) => ({
            ...entry,
            userName: userMap[entry.studentId] || "Unknown"
        }));

        return enrichedData;
    } catch (error) {
        console.error("Error fetching students health with user names:", error);
        return [];
    }
};

export const setStudentHealth = async (studentId, schoolId, healthStatus) => {
    try {
        const docRef = db.collection("students-health").doc(studentId, schoolId);
        const doc = await docRef.get();
        if (!doc.exists) {
            await docRef.set({ studentId, schoolId, healthStatus });
            console.log(`Student health status set successfully for ID ${studentId}.`);
            return `Student health status set successfully for ID ${studentId}.`;
        }
        await docRef.update({ healthStatus });
        console.log(`Student health status updated successfully for ID ${studentId}.`);
        return `Student health status updated successfully for ID ${studentId}.`;
    } catch (error) {
        console.error("Error updating student health status:", error);
        return null;
    }
}