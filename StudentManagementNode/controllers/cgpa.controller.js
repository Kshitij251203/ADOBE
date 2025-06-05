import Marks from '../models/Marks.js';
import Subject from '../models/Subject.js';

export const calculateCGPA = async (req, res) => {
  try {
    const marks = await Marks.find({ studentId: req.params.id }).populate('subjectId');
    if (!marks.length) return res.status(404).json({ error: 'No marks found for student' });

    let totalCredits = 0;
    let totalWeightedScore = 0;

    for (let entry of marks) {
      const { marks, subjectId } = entry;
      const credits = subjectId.credits;

      
      let gradePoint = 0;
      if (marks >= 90) gradePoint = 10;
      else if (marks >= 80) gradePoint = 9;
      else if (marks >= 70) gradePoint = 8;
      else if (marks >= 60) gradePoint = 7;
      else if (marks >= 50) gradePoint = 6;
      else gradePoint = 0;

      totalCredits += credits;
      totalWeightedScore += gradePoint * credits;
    }

    const cgpa = (totalWeightedScore / totalCredits).toFixed(2);
    res.json({ cgpa });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};
