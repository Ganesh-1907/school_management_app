import { db } from "../configs/firebase-config.js";

export const addAnnouncement = async (announcement) => {
    try {
        const docRef = await db.collection("announcements").add(announcement);
        console.log("Announcement added with ID:", docRef.id);
        return { id: docRef.id, ...announcement };
    } catch (error) {
        console.error("Error adding announcement:", error);
        return null;
    }
}

export const getAnnouncements = async (schoolId) => {
    try {
        const snapshot = await db.collection("announcements").where("schoolId", "==", schoolId).get();
        const announcements = [];
        snapshot.forEach((doc) => {
            announcements.push({ id: doc.id, ...doc.data() });
        });
        return announcements;
    } catch (error) {
        console.error("Error fetching announcements:", error);
        return [];
    }
}

export const addAttendance = async (attendanceArray) => {
    try {
        const addedRecords = [];
        for (const attendance of attendanceArray) {
            attendance.date = new Date().toISOString(); // Add current date
            const docRef = await db.collection("attendance").add(attendance); // Save to Firestore
            console.log("Attendance added with ID:", docRef.id);
            addedRecords.push({ id: docRef.id, ...attendance });
        }
        return addedRecords;
    } catch (error) {
        console.error("Error adding attendance:", error);
        return null;
    }
};



export const getAttendance = async (schoolId, className) => {
    try {
        const snapshot = await db.collection("attendance").where("schoolId", "==", schoolId).where("class", "==", className).get();
        const attendance = [];
        snapshot.forEach((doc) => {
            attendance.push({ id: doc.id, ...doc.data() });
        });
        return attendance;
    } catch (error) {
        console.error("Error fetching attendance:", error);
        return [];
    }
}

export const addMarks = async (marks) => {
    try {
        const docRef = await db.collection("marks").add(marks);
        console.log("Marks added with ID:", docRef.id);
        return { id: docRef.id, ...marks };
    } catch (error) {
        console.error("Error adding marks:", error);
        return null;
    }
}

export const getMarks = async (schoolId, className) => {
    try {
        const snapshot = await db.collection("marks").where("schoolId", "==", schoolId).where("class", "==", className).get();
        const marks = [];
        snapshot.forEach((doc) => {
            marks.push({ id: doc.id, ...doc.data() });
        });
        return marks;
    } catch (error) {
        console.error("Error fetching marks:", error);
        return [];
    }
}