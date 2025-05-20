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