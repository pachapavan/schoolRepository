import { IClassName } from 'app/shared/model/class-name.model';
import { IStudentMarkes } from 'app/shared/model/student-markes.model';
import { IAttendence } from 'app/shared/model/attendence.model';
import { IStudentFee } from 'app/shared/model/student-fee.model';
import { IBusRoute } from 'app/shared/model/bus-route.model';

export interface IStudent {
  id?: number;
  studentId?: number;
  studentName?: string;
  parentName?: string;
  phoneNumber?: number;
  address?: string;
  photoContentType?: string;
  photo?: any;
  status?: string;
  comments?: string;
  classes?: IClassName[];
  markes?: IStudentMarkes[];
  attendences?: IAttendence[];
  fees?: IStudentFee[];
  busRouteNames?: IBusRoute[];
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public studentId?: number,
    public studentName?: string,
    public parentName?: string,
    public phoneNumber?: number,
    public address?: string,
    public photoContentType?: string,
    public photo?: any,
    public status?: string,
    public comments?: string,
    public classes?: IClassName[],
    public markes?: IStudentMarkes[],
    public attendences?: IAttendence[],
    public fees?: IStudentFee[],
    public busRouteNames?: IBusRoute[]
  ) {}
}
