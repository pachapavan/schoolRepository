import * as dayjs from 'dayjs';
import { IStudent } from 'app/entities/student/student.model';

export interface IAttendence {
  id?: number;
  month?: dayjs.Dayjs | null;
  totalWorkingDays?: number | null;
  dayspresent?: number | null;
  student?: IStudent | null;
}

export class Attendence implements IAttendence {
  constructor(
    public id?: number,
    public month?: dayjs.Dayjs | null,
    public totalWorkingDays?: number | null,
    public dayspresent?: number | null,
    public student?: IStudent | null
  ) {}
}

export function getAttendenceIdentifier(attendence: IAttendence): number | undefined {
  return attendence.id;
}
