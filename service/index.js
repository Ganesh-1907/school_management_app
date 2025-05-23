import express from "express";
import { addOfficer, addSchools } from "./components/manual-creation-script.js";
import { addCommodity, getCommodity, getSchoolById, getSchoolByName, getSchools, mapSchoolWithUser } from "./components/school.js";
import { createUser, fetchUsersNotInSchool, getUserByEmail, getUserById, getUsers, loginUser } from "./components/user.js";
import { UserRole } from "./utils/enums.js";
import { addStudent, getStudents, getStudentsHealth, setStudentHealth } from "./components/student.js";
import { addAnnouncement, addAttendance, getAnnouncements, getAttendance } from "./components/common.js";

const app = express();
app.use(express.json());

addOfficer();

addSchools();

// user routes
app.get('/get-users/:role', async (req, res) => {
    const { role } = req.params;
    const result = await getUsers(role);
    res.send(result);
});

app.post('/add-user', async (req, res) => {
    const body = req.body;
    const result = await createUser(body);
    res.send(result);
})

app.post('/login', async (req, res) => {
    const { email, password } = req.body;
    const result = await loginUser(email, password);
    if (result) {
        res.send({ message: "Login successful", user: result });
    } else {
        res.status(401).send({ message: "Invalid credentials" });
    }
});

app.get('/get-user/:userId', async (req, res) => {
    const userId = req.params.userId;
    const result = await getUserById(userId);
    res.send(result);
});

app.get('/get-user-by-email/:email', async (req, res) => {
    const email = req.params.email;
    const result = await getUserByEmail(email);
    res.send(result);
});

// school routes
app.get('/add-student', async (req, res) => {
    const result = await addStudent(req.body);
    res.send(result);
});

app.get('/get-students/:schoolId', async (req, res) => {
    const { schoolId } = req.params;
    const result = await getStudents(schoolId);
    res.send(result);
})

app.get('/get-schools', async (req, res) => {
    const result = await getSchools();
    res.send(result);
});

// getSchoolById("ARKT333rVPpY2ZAtej6h")
app.get('/get-school/:schoolId', async (req, res) => {
    const schoolId = req.params.schoolId;
    console.log(schoolId,'schoolID man')
    const result = await getSchoolById(schoolId);
    res.send(result);
});

app.get('/get-school-by-name/:schoolName', async (req, res) => {
    const schoolName = req.params.schoolName;
    const result = await getSchoolByName(schoolName);
    res.send(result);
});

// school-user mapping
app.get('/fetch-users-not-in-school', async (req, res) => {
    const result = await fetchUsersNotInSchool();
    res.send(result);
});

app.post('/map-school-with-user', async (req, res) => {
    const { schoolId, userId, role } = req.body;
    const result = await mapSchoolWithUser(schoolId, userId, role);
    res.send(result);
});

// student health routes
app.get('/get-students-health/:schoolId', async (req, res) => {
    const { schoolId } = req.params;
    console.log("schoolId", schoolId);
    const result = await getStudentsHealth(schoolId);
    res.send(result);
});

app.post('/set-student-health', async (req, res) => {
    const { studentId, schoolId, healthStatus } = req.body;
    const result = await setStudentHealth(studentId, schoolId, healthStatus);
    res.send(result);
});

// student comodity routes
app.get('/get-students-commodity/:schoolId/', async (req, res) => {
    const { schoolId } = req.params;
    console.log("schoolId", schoolId);
    const result = await getCommodity(schoolId);
    console.log(result,'result')
    res.send(result);
});

app.post('/set-student-commodity', async (req, res) => {
    const { schoolId, month, phase1, phase2, title } = req.body;
    const result = await addCommodity(schoolId, month, phase1, phase2, title);
    res.send(result);
});

// announcement routes
app.post('/add-announcement', async (req, res) => {
    const { schoolId, title, description } = req.body;
    const result = await addAnnouncement({ schoolId, title, description });
    res.send(result);
});

app.get('/get-announcements/:schoolId', async (req, res) => {
    const { schoolId } = req.params;
    const result = await getAnnouncements(schoolId);
    res.send(result);
});

// attendance routes
app.post('/add-attendance', async (req, res) => {
    const { schoolId, userId, date, present, class: className } = req.body;
    const result = await addAttendance({ schoolId, userId, date, present, class: className });
    res.send(result);
});

app.get('/get-attendance/:schoolId/:class', async (req, res) => {
    const { schoolId, class: className } = req.params;
    const result = await getAttendance(schoolId, className);
    res.send(result);
});

app.listen(3000, () => {
    console.log("Server is running on port 3000");
});