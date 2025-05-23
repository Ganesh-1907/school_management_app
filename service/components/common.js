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

export const addAttendance = async (attendance) => {
    try {
        const docRef = await db.collection("attendance").add(attendance);
        console.log("Attendance added with ID:", docRef.id);
        return { id: docRef.id, ...attendance };
    } catch (error) {
        console.error("Error adding attendance:", error);
        return null;
    }
}

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