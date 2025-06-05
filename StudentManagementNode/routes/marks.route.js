import express from 'express';
import {
  getAllMarks,
  getMarksById,
  createMarks,
  updateMarks,
  getMarksByStudentId
} from '../controllers/marks.controller.js';

const router = express.Router();

router.get('/', getAllMarks);
router.get('/:id', getMarksById);
router.post('/', createMarks);
router.put('/:id', updateMarks);

router.get('/student/:id', getMarksByStudentId);

export default router;
