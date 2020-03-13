import { ISubject } from 'app/shared/model/subject.model';
import { IClassName } from 'app/shared/model/class-name.model';
import { IStudent } from 'app/shared/model/student.model';

export interface IStudentMarkes {
  id?: number;
  examName?: string;
  totalMarkes?: number;
  markes?: number;
  comments?: string;
  subjects?: ISubject[];
  classes?: IClassName[];
  student?: IStudent;
}

export class StudentMarkes implements IStudentMarkes {
  constructor(
    public id?: number,
    public examName?: string,
    public totalMarkes?: number,
    public markes?: number,
    public comments?: string,
    public subjects?: ISubject[],
    public classes?: IClassName[],
    public student?: IStudent
  ) {}
}
