import { IStudent } from 'app/entities/student/student.model';

export interface IStudentFee {
  id?: number;
  totalAcademicFee?: number | null;
  academicFeewaveOff?: number | null;
  academicFeePaid?: number | null;
  totalAcademicFeePaid?: number | null;
  academicFeepending?: number | null;
  busAlloted?: boolean | null;
  hostelAlloted?: boolean | null;
  totalBusFee?: number | null;
  busFeewaveOff?: number | null;
  busFeePaid?: number | null;
  totalBusFeePaid?: number | null;
  busFeepending?: number | null;
  totalHostelFee?: number | null;
  hostelFeewaveOff?: number | null;
  totalHostelFeePaid?: number | null;
  hostelFeePaid?: number | null;
  hostelFeepending?: number | null;
  hostelExpenses?: number | null;
  year?: number | null;
  comments?: string | null;
  student?: IStudent | null;
}

export class StudentFee implements IStudentFee {
  constructor(
    public id?: number,
    public totalAcademicFee?: number | null,
    public academicFeewaveOff?: number | null,
    public academicFeePaid?: number | null,
    public totalAcademicFeePaid?: number | null,
    public academicFeepending?: number | null,
    public busAlloted?: boolean | null,
    public hostelAlloted?: boolean | null,
    public totalBusFee?: number | null,
    public busFeewaveOff?: number | null,
    public busFeePaid?: number | null,
    public totalBusFeePaid?: number | null,
    public busFeepending?: number | null,
    public totalHostelFee?: number | null,
    public hostelFeewaveOff?: number | null,
    public totalHostelFeePaid?: number | null,
    public hostelFeePaid?: number | null,
    public hostelFeepending?: number | null,
    public hostelExpenses?: number | null,
    public year?: number | null,
    public comments?: string | null,
    public student?: IStudent | null
  ) {
    this.busAlloted = this.busAlloted ?? false;
    this.hostelAlloted = this.hostelAlloted ?? false;
  }
}

export function getStudentFeeIdentifier(studentFee: IStudentFee): number | undefined {
  return studentFee.id;
}
