import { IStudentMarkes } from 'app/entities/student-markes/student-markes.model';

export interface ISubject {
  id?: number;
  classname?: string | null;
  section?: string | null;
  subjectName?: string | null;
  subjectCode?: string | null;
  subjectTeacher?: string | null;
  studentMarkes?: IStudentMarkes | null;
}

export class Subject implements ISubject {
  constructor(
    public id?: number,
    public classname?: string | null,
    public section?: string | null,
    public subjectName?: string | null,
    public subjectCode?: string | null,
    public subjectTeacher?: string | null,
    public studentMarkes?: IStudentMarkes | null
  ) {}
}

export function getSubjectIdentifier(subject: ISubject): number | undefined {
  return subject.id;
}
