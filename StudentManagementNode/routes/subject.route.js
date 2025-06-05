import express from 'express';
import {
  getAllSubjects,
  getSubjectById,
  createSubject,
  updateSubject
} from '../controllers/subject.controller.js';

const router = express.Router();

router.get('/', getAllSubjects);
router.get('/:id', getSubjectById);
router.post('/', createSubject);
router.put('/:id', updateSubject);


export default router;
