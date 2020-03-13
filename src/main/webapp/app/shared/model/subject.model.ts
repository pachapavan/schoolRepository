import { IStudentMarkes } from 'app/shared/model/student-markes.model';

export interface ISubject {
  id?: number;
  classname?: string;
  section?: string;
  subjectName?: string;
  subjectCode?: string;
  subjectTeacher?: string;
  studentMarkes?: IStudentMarkes;
}

export class Subject implements ISubject {
  constructor(
    public id?: number,
    public classname?: string,
    public section?: string,
    public subjectName?: string,
    public subjectCode?: string,
    public subjectTeacher?: string,
    public studentMarkes?: IStudentMarkes
  ) {}
}
