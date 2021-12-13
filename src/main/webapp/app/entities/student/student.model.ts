import { IClassName } from 'app/entities/class-name/class-name.model';
import { IStudentMarkes } from 'app/entities/student-markes/student-markes.model';
import { IAttendence } from 'app/entities/attendence/attendence.model';
import { IStudentFee } from 'app/entities/student-fee/student-fee.model';
import { IBusRoute } from 'app/entities/bus-route/bus-route.model';

export interface IStudent {
  id?: number;
  studentId?: number | null;
  studentName?: string | null;
  parentName?: string | null;
  phoneNumber?: number | null;
  address?: string | null;
  photoContentType?: string | null;
  photo?: string | null;
  status?: string | null;
  comments?: string | null;
  classes?: IClassName[] | null;
  markes?: IStudentMarkes[] | null;
  attendences?: IAttendence[] | null;
  fees?: IStudentFee[] | null;
  busRouteNames?: IBusRoute[] | null;
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public studentId?: number | null,
    public studentName?: string | null,
    public parentName?: string | null,
    public phoneNumber?: number | null,
    public address?: string | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public status?: string | null,
    public comments?: string | null,
    public classes?: IClassName[] | null,
    public markes?: IStudentMarkes[] | null,
    public attendences?: IAttendence[] | null,
    public fees?: IStudentFee[] | null,
    public busRouteNames?: IBusRoute[] | null
  ) {}
}

export function getStudentIdentifier(student: IStudent): number | undefined {
  return student.id;
}
