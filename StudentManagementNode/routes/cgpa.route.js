import express from 'express';
import { calculateCGPA } from '../controllers/cgpa.controller.js';

const router = express.Router();

router.get('/student/:id', calculateCGPA);

export default router;
