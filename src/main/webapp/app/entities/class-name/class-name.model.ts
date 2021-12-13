import { ISection } from 'app/entities/section/section.model';
import { IStudent } from 'app/entities/student/student.model';
import { IStudentMarkes } from 'app/entities/student-markes/student-markes.model';
import { IStaff } from 'app/entities/staff/staff.model';

export interface IClassName {
  id?: number;
  name?: string | null;
  classNumber?: number | null;
  sections?: ISection[] | null;
  student?: IStudent | null;
  studentMarkes?: IStudentMarkes | null;
  staff?: IStaff | null;
}

export class ClassName implements IClassName {
  constructor(
    public id?: number,
    public name?: string | null,
    public classNumber?: number | null,
    public sections?: ISection[] | null,
    public student?: IStudent | null,
    public studentMarkes?: IStudentMarkes | null,
    public staff?: IStaff | null
  ) {}
}

export function getClassNameIdentifier(className: IClassName): number | undefined {
  return className.id;
}
