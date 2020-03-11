import { Moment } from 'moment';
import { IStudent } from 'app/shared/model/student.model';

export interface IAttendence {
  id?: number;
  month?: Moment;
  totalWorkingDays?: number;
  dayspresent?: number;
  student?: IStudent;
}

export class Attendence implements IAttendence {
  constructor(
    public id?: number,
    public month?: Moment,
    public totalWorkingDays?: number,
    public dayspresent?: number,
    public student?: IStudent
  ) {}
}
