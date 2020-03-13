import { ISection } from 'app/shared/model/section.model';
import { IStudent } from 'app/shared/model/student.model';
import { IStudentMarkes } from 'app/shared/model/student-markes.model';
import { IStaff } from 'app/shared/model/staff.model';

export interface IClassName {
  id?: number;
  name?: string;
  classNumber?: number;
  sections?: ISection[];
  student?: IStudent;
  studentMarkes?: IStudentMarkes;
  staff?: IStaff;
}

export class ClassName implements IClassName {
  constructor(
    public id?: number,
    public name?: string,
    public classNumber?: number,
    public sections?: ISection[],
    public student?: IStudent,
    public studentMarkes?: IStudentMarkes,
    public staff?: IStaff
  ) {}
}
