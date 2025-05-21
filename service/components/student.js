import { db } from "../configs/firebase-config.js";

export const addStudent = async (userData) => {
    try {
        const existUser = await db.collection("users").where("email", "==", userData.email).get();
        if (!existUser.empty) {
            console.log(`User with email ${userData.email} already exists.`);
            return `User with email ${userData.email} already exists.`;
        }
        await db.collection("users").doc().set({
            name: userData.name,
            email: userData.email,
            role: userData.role,
            password: userData.phone,
            class: userData.class,
            phone: userData.phone,
            address: userData.address,
            dateOfBirth: userData.dateOfBirth,
            gender: userData.gender,
            joiningDate: userData.joiningDate,
        });
        await db.collection('users-school').doc().set({
            schoolId: userData.schoolId,
            userId: userData.userId,
            role: userData.role,
            joinedAt: new Date().toISOString()
        });
        console.log(`User ${userData.name} created successfully!`);
        return `User ${userData.name} created successfully!`;
    } catch (error) {
        console.error("Error creating user:", error);
        return `Error creating user: ${error.message}`;
    }
}

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
                const userData = userDoc.data();
                userMap[userId] = userData.name || "Unknown";
            } else {
                userMap[userId] = "Unknown";
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