import { IStaff } from 'app/entities/staff/staff.model';

export interface IStaffSalary {
  id?: number;
  salaryPaid?: number | null;
  month?: string | null;
  staff?: IStaff | null;
}

export class StaffSalary implements IStaffSalary {
  constructor(public id?: number, public salaryPaid?: number | null, public month?: string | null, public staff?: IStaff | null) {}
}

export function getStaffSalaryIdentifier(staffSalary: IStaffSalary): number | undefined {
  return staffSalary.id;
}
