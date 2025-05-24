import { db } from "../configs/firebase-config.js";

export const getUsers = async (role) => {
    try {
        const snapshot = await db.collection("users").where("role", "==", role).get();
        const users = [];
        snapshot.forEach((doc) => {
            users.push({ id: doc.id, ...doc.data() });
        });
        return users;
    } catch (error) {
        console.error("Error fetching users:", error);
        return [];
    }
}

export const loginUser = async (email, password) => {
    try {
        const snapshot = await db.collection("users").where("email", "==", email).get();
        if (snapshot.empty) {
            console.log(`User with email ${email} not found.`);
            return null;
        }
        let user = null;
        const schoolData = await db.collection("users-school").where("userId", "==", snapshot.docs[0].id).get();
        user = { id: snapshot.docs[0].id, ...snapshot.docs[0].data(), schoolId: schoolData.docs[0]?.data().schoolId || null };
        if (user.password !== password) {
            console.log("Invalid password.");
            return null;
        }
        return user;
    } catch (error) {
        console.error("Error logging in user:", error);
        return null;
    }
}

export const getUserById = async (userId) => {
    try {
        const doc = await db.collection("users").doc(userId).get();
        if (!doc.exists) {
            console.log(`User with ID ${userId} not found.`);
            return null;
        }
        return { id: doc.id, ...doc.data() };
    }
    catch (error) {
        console.error("Error fetching user:", error);
        return null;
    }
}

export const getUserByEmail = async (email) => {
    try {
        const snapshot = await db.collection("users").where("email", "==", email).get();
        if (snapshot.empty) {
            console.log(`User with email ${email} not found.`);
            return null;
        }
        const users = [];
        snapshot.forEach((doc) => {
            users.push({ id: doc.id, ...doc.data() });
        });
        return users;
    } catch (error) {
        console.error("Error fetching user:", error);
        return null;
    }
}

export const createUser = async (userData) => {
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
            phone: userData.phone,
            address: userData.address,
            dateOfBirth: userData.dateOfBirth,
            gender: userData.gender,
            joiningDate: userData.joiningDate,
        });
        console.log(`User ${userData.name} created successfully!`);
        return `User ${userData.name} created successfully!`;
    } catch (error) {
        console.error("Error creating user:", error);
        return `Error creating user: ${error.message}`;
    }
}

export const fetchUsersNotInSchool = async () => {
    try {
        const schoolSnapshot = await db.collection("users-school").get();
        const schoolUserIds = new Set();
        schoolSnapshot.forEach(doc => {
            const data = doc.data();
            if (data.userId) {
                schoolUserIds.add(data.userId);
            }
        });
        const usersSnapshot = await db.collection("users").get();
        const usersNotInSchool = [];
        usersSnapshot.forEach(doc => {
            const userData = doc.id;
            if (!schoolUserIds.has(userData)) {
                usersNotInSchool.push({ id: doc.id, ...doc.data() });
            }
        });
        console.log("Users not in any school:", usersNotInSchool);
        return usersNotInSchool;
    } catch (error) {
        console.error("Error fetching users not in school:", error);
        return null;
    }
};
