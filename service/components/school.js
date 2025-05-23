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
