import { db } from "../configs/firebase-config.js";

export const getSchools = async () => {
    try {
        const snapshot = await db.collection("schools").get();
        const schools = [];
        snapshot.forEach((doc) => {
            schools.push({ id: doc.id, ...doc.data() });
        });
        // console.log("Schools fetched successfully:", schools);
        return schools;
    } catch (error) {
        console.error("Error fetching schools:", error);
        return [];
    }
}

export const getSchoolById = async (schoolId) => {
    try {
        const doc = await db.collection("schools").doc(schoolId).get();
        if (!doc.exists) {
            console.log(`School with ID ${schoolId} not found.`);
            return null;
        }
        // console.log("School fetched successfully:", { id: doc.id, ...doc.data() });
        return { id: doc.id, ...doc.data() };
    } catch (error) {
        console.error("Error fetching school:", error);
        return null;
    }
}

export const getSchoolByName = async (schoolName) => {
    try {
        const snapshot = await db.collection("schools").where("schoolName", "==", schoolName).get();
        if (snapshot.empty) {
            console.log(`School with name ${schoolName} not found.`);
            return null;
        }
        let school = null;
        snapshot.forEach((doc) => {
            school = { id: doc.id, ...doc.data() };
        });
        // console.log("School fetched successfully By Name:", school);
        return school;
    } catch (error) {
        console.error("Error fetching school:", error);
        return null;
    }
}

export const mapSchoolWithUser = async (schoolId, userId, role) => {
    try {
        const snapshot = await db.collection("users-school").where("userId", "==", userId).get();
        if (!snapshot.empty) {
            console.log(`User with ID ${userId} already mapped to a school.`);
            return `User with ID ${userId} already mapped to a school.`;
        }
        const mapSchool = await db.collection("users-school").doc().set({
            schoolId: schoolId,
            userId: userId,
            role: role,
            joinedAt: new Date().toISOString()
        })
        console.log("School mapped with user successfully:", mapSchool);
        return mapSchool;
    } catch (error) {
        console.error("Error mapping school with user:", error);
    }
}

export const addCommodity = async (schoolId, month, phase1, phase2, title) => {
    try {
        const snapshot = await db.collection("commodities").where("schoolId", "==", schoolId).where("month", "==", month).where("title", "==", title).get();
        if (!snapshot.empty) {
            const updateDoc = snapshot.docs[0];
            await db.collection("commodities").doc(updateDoc.id).update({
                phase1: phase1,
                phase2: phase2,
                updatedAt: new Date().toISOString()
            });
            console.log("Commodity updated successfully:", updateDoc.id);
            return updateDoc.id;
        }
        const docRef = await db.collection("commodities").add({
            schoolId: schoolId,
            month: month,
            phase1: phase1,
            phase2: phase2,
            title: title,
            createdAt: new Date().toISOString()
        });
        console.log("Commodity added successfully:", docRef.id);
        return docRef.id;
    } catch (error) {
        console.error("Error adding commodity:", error);
        return null;
    }
}

export const getCommodity = async (schoolId) => {
    try {
        const snapshot = await db.collection("commodities").where("schoolId", "==", schoolId).get();
        if (snapshot.empty) {
            // console.log(`Commodity with school ID ${schoolId} and month ${month} not found.`);
            return null;
        }
        let commodity = [];
        snapshot.forEach((doc) => {
            commodity.push({ id: doc.id, ...doc.data() });
        });
        // console.log("Commodity fetched successfully:", commodity);
        return commodity;
    } catch (error) {
        console.error("Error fetching commodity:", error);
        return null;
    }
}

export const staffDetails = async (schoolId) => {
    try {
        const snapshot = await db.collection("users-school").where("schoolId", "==", schoolId).get();
        const staffDetails = [];
        await Promise.all(snapshot.docs.map(async (doc) => {
            const userData = await db.collection("users").doc(doc.data().userId).get();
            staffDetails.push({ id: doc.id, ...doc.data(), name: userData.data().name, email: userData.data().email, phone: userData.data().phone, role: doc.data().role });
        }));
        const principal = staffDetails.find(staff => staff.role === 'Principal');
        const warden = staffDetails.find(staff => staff.role === 'Warden');
        const teachers = staffDetails.filter(staff => staff.role === 'Teacher');
        const cookingStaff = staffDetails.filter(staff => staff.role === 'Cooking');

        return [
            ...(principal ? [principal] : []),
            ...teachers,
            ...(warden ? [warden] : []),
            ...(cookingStaff.length > 0 ? cookingStaff : [])
        ];
    } catch (error) {
        console.error("Error fetching staff details:", error);
        return null;
    }
}

export const addStaffSalary = async (schoolId, userId, salary, issueDate) => {
    try {
        const snapshot = await db.collection("users-school").where("userId", "==", userId).where("schoolId", "==", schoolId).get();
        if (snapshot.empty) {
            console.log(`User with ID ${userId} not found in school ${schoolId}.`);
            return null;
        }
        const checkSnapshot = await db.collection("staff-salary").where("userId", "==", userId).where("schoolId", "==", schoolId).where("issueDate", "==", issueDate).get();
        if (!checkSnapshot.empty) {
            console.log(`Salary already issued for user ${userId} in school ${schoolId} on ${issueDate}.`);
            return null;
        }
        const docRef = await db.collection("staff-salary").add({
            schoolId: schoolId,
            userId: userId,
            salary: salary,
            issueDate: issueDate,
            createdAt: new Date().toISOString()
        });
        console.log("Staff salary added successfully:", docRef.id);
        return docRef.id;
    } catch (error) {
        console.error("Error adding staff salary:", error);
        return null;
    }
}

export const getStaffSalary = async (schoolId) => {
    try {
        const snapshot = await db.collection("staff-salary").where("schoolId", "==", schoolId).get();
        if (snapshot.empty) {
            console.log(`No staff salary found for school ID ${schoolId}.`);
            return null;
        }
        let staffSalary = [];
        await Promise.all(snapshot.docs.map(async (doc) => {
            const userData = await db.collection("users").doc(doc.data().userId).get();
            staffSalary.push({ id: doc.id, ...doc.data(), name: userData.data().name, email: userData.data().email, phone: userData.data().phone });
        }));
        return staffSalary;
    } catch (error) {
        console.error("Error fetching staff salary:", error);
        return null;
    }
}

export const cookingStaffDetails = async (schoolId) => {
    try {
        const snapshot = await db.collection("users-school").where("schoolId", "==", schoolId).where("role", "==", "Cooking Staff").get();
        const cookingStaffDetails = [];
        await Promise.all(snapshot.docs.map(async (doc) => {
            const userData = await db.collection("users").doc(doc.data().userId).get();
            cookingStaffDetails.push({ id: doc.id, ...doc.data(), name: userData.data().name, email: userData.data().email, phone: userData.data().phone });
        }));
        return cookingStaffDetails;
    } catch (error) {
        console.error("Error fetching cooking staff details:", error);
        return null;
    }
}