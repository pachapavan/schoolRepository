import { IStaffSalary } from 'app/shared/model/staff-salary.model';
import { IClassName } from 'app/shared/model/class-name.model';

export interface IStaff {
  id?: number;
  staffId?: number;
  staffName?: string;
  phoneNumber?: number;
  address?: string;
  photoContentType?: string;
  photo?: any;
  isTeachingStaff?: boolean;
  status?: string;
  salary?: number;
  salaries?: IStaffSalary[];
  teachers?: IClassName[];
}

export class Staff implements IStaff {
  constructor(
    public id?: number,
    public staffId?: number,
    public staffName?: string,
    public phoneNumber?: number,
    public address?: string,
    public photoContentType?: string,
    public photo?: any,
    public isTeachingStaff?: boolean,
    public status?: string,
    public salary?: number,
    public salaries?: IStaffSalary[],
    public teachers?: IClassName[]
  ) {
    this.isTeachingStaff = this.isTeachingStaff || false;
  }
}
