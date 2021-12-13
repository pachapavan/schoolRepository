import { ISubject } from 'app/entities/subject/subject.model';
import { IClassName } from 'app/entities/class-name/class-name.model';
import { IStudent } from 'app/entities/student/student.model';

export interface IStudentMarkes {
  id?: number;
  examName?: string | null;
  totalMarkes?: number | null;
  markes?: number | null;
  comments?: string | null;
  subjects?: ISubject[] | null;
  classes?: IClassName[] | null;
  student?: IStudent | null;
}

export class StudentMarkes implements IStudentMarkes {
  constructor(
    public id?: number,
    public examName?: string | null,
    public totalMarkes?: number | null,
    public markes?: number | null,
    public comments?: string | null,
    public subjects?: ISubject[] | null,
    public classes?: IClassName[] | null,
    public student?: IStudent | null
  ) {}
}

export function getStudentMarkesIdentifier(studentMarkes: IStudentMarkes): number | undefined {
  return studentMarkes.id;
}
