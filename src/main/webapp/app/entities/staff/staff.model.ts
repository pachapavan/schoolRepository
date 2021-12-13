import { IStaffSalary } from 'app/entities/staff-salary/staff-salary.model';
import { IClassName } from 'app/entities/class-name/class-name.model';

export interface IStaff {
  id?: number;
  staffId?: number | null;
  staffName?: string | null;
  phoneNumber?: number | null;
  address?: string | null;
  photoContentType?: string | null;
  photo?: string | null;
  isTeachingStaff?: boolean | null;
  status?: string | null;
  salary?: number | null;
  salaries?: IStaffSalary[] | null;
  teachers?: IClassName[] | null;
}

export class Staff implements IStaff {
  constructor(
    public id?: number,
    public staffId?: number | null,
    public staffName?: string | null,
    public phoneNumber?: number | null,
    public address?: string | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public isTeachingStaff?: boolean | null,
    public status?: string | null,
    public salary?: number | null,
    public salaries?: IStaffSalary[] | null,
    public teachers?: IClassName[] | null
  ) {
    this.isTeachingStaff = this.isTeachingStaff ?? false;
  }
}

export function getStaffIdentifier(staff: IStaff): number | undefined {
  return staff.id;
}
