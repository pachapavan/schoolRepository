import { IStudent } from 'app/shared/model/student.model';

export interface IStudentFee {
  id?: number;
  totalAcademicFee?: number;
  academicFeewaveOff?: number;
  academicFeePaid?: number;
  totalAcademicFeePaid?: number;
  academicFeepending?: number;
  busAlloted?: boolean;
  hostelAlloted?: boolean;
  totalBusFee?: number;
  busFeewaveOff?: number;
  busFeePaid?: number;
  totalBusFeePaid?: number;
  busFeepending?: number;
  totalHostelFee?: number;
  hostelFeewaveOff?: number;
  totalHostelFeePaid?: number;
  hostelFeePaid?: number;
  hostelFeepending?: number;
  hostelExpenses?: number;
  year?: number;
  comments?: string;
  student?: IStudent;
}

export class StudentFee implements IStudentFee {
  constructor(
    public id?: number,
    public totalAcademicFee?: number,
    public academicFeewaveOff?: number,
    public academicFeePaid?: number,
    public totalAcademicFeePaid?: number,
    public academicFeepending?: number,
    public busAlloted?: boolean,
    public hostelAlloted?: boolean,
    public totalBusFee?: number,
    public busFeewaveOff?: number,
    public busFeePaid?: number,
    public totalBusFeePaid?: number,
    public busFeepending?: number,
    public totalHostelFee?: number,
    public hostelFeewaveOff?: number,
    public totalHostelFeePaid?: number,
    public hostelFeePaid?: number,
    public hostelFeepending?: number,
    public hostelExpenses?: number,
    public year?: number,
    public comments?: string,
    public student?: IStudent
  ) {
    this.busAlloted = this.busAlloted || false;
    this.hostelAlloted = this.hostelAlloted || false;
  }
}
