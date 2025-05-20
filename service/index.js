import { createUser, getUserByEmail, getUserById, getUsers, loginUser } from "./components/user.js";
import { addOfficer, addSchools } from "./components/manual-creation-script.js";
import express from "express";

const app = express();
app.use(express.json());

addOfficer();

addSchools();

app.get('/get-users', async (req, res) => {
    const result = await getUsers()
    res.send(result);
});

app.get('/login', async (req, res) => {
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

app.post('/add-user', async (req, res) => {
    const body = req.body;
    const result = await createUser(body);
    res.send(result);
})

app.listen(3000, () => {
    console.log("Server is running on port 3000");
});