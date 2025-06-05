import Marks from '../models/Marks.js';

export const getAllMarks = async (req, res) => {
  try {
    const marks = await Marks.find().populate('studentId subjectId');
    res.json(marks);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

export const getMarksById = async (req, res) => {
  try {
    const mark = await Marks.findById(req.params.id).populate('studentId subjectId');
    if (!mark) return res.status(404).json({ error: 'Marks not found' });
    res.json(mark);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

export const createMarks = async (req, res) => {
  try {
    const { studentId, subjectId, semester } = req.body;

    
    const existingMark = await Marks.findOne({ studentId, subjectId, semester });
    if (existingMark) {
      return res.status(400).json({ error: 'Marks for this student, subject, and semester already exist.' });
    }

    const newMark = new Marks(req.body);
    await newMark.save();
    res.status(201).json(newMark);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
};


export const updateMarks = async (req, res) => {
  try {
    const updated = await Marks.findByIdAndUpdate(req.params.id, req.body, { new: true });
    if (!updated) return res.status(404).json({ error: 'Marks not found' });
    res.json(updated);
  } catch (err) {
    res.status(400).json({ error: err.message });
  }
};

export const getMarksByStudentId = async (req, res) => {
  try {
    const marks = await Marks.find({ studentId: req.params.id }).populate('subjectId');
    res.json(marks);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};
