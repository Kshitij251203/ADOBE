import Semester from '../models/Semester.js';
import Subject from '../models/Subject.js';

export const getAllSemesters = async (req, res) => {
  try {
    const semesters = await Semester.find().populate('subjects');
    res.json(semesters);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

export const getSemesterById = async (req, res) => {
  try {
    const semester = await Semester.findById(req.params.id).populate('subjects');
    if (!semester) return res.status(404).json({ error: 'Semester not found' });
    res.json(semester);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

export const createSemester = async (req, res) => {
  try {
    const { semesterNumber, name, subjectIds } = req.body;

    // Step 1: Validate subjectIds exist in Subject collection
    if (subjectIds && subjectIds.length > 0) {
      const foundSubjects = await Subject.find({ _id: { $in: subjectIds } });

      if (foundSubjects.length !== subjectIds.length) {
        return res.status(400).json({
          error: 'One or more subjectIds are invalid or do not exist.'
        });
      }
    }

    // Step 2: Create the semester
    const newSemester = new Semester({
      semesterNumber,
      name,
      subjects: subjectIds || []
    });

    await newSemester.save();
    const populated = await newSemester.populate('subjects');

    res.status(201).json(populated);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
};



export const updateSemester = async (req, res) => {
  try {
    const updated = await Semester.findByIdAndUpdate(req.params.id, req.body, { new: true });
    if (!updated) return res.status(404).json({ error: 'Semester not found' });
    res.json(updated);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
};

