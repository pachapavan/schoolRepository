import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { StudentFeeService } from 'app/entities/student-fee/student-fee.service';
import { IStudentFee, StudentFee } from 'app/shared/model/student-fee.model';

describe('Service Tests', () => {
  describe('StudentFee Service', () => {
    let injector: TestBed;
    let service: StudentFeeService;
    let httpMock: HttpTestingController;
    let elemDefault: IStudentFee;
    let expectedResult: IStudentFee | IStudentFee[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(StudentFeeService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new StudentFee(0, 0, 0, 0, 0, 0, false, false, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a StudentFee', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new StudentFee()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a StudentFee', () => {
        const returnedFromService = Object.assign(
          {
            totalAcademicFee: 1,
            academicFeewaveOff: 1,
            academicFeePaid: 1,
            totalAcademicFeePaid: 1,
            academicFeepending: 1,
            busAlloted: true,
            hostelAlloted: true,
            totalBusFee: 1,
            busFeewaveOff: 1,
            busFeePaid: 1,
            totalBusFeePaid: 1,
            busFeepending: 1,
            totalHostelFee: 1,
            hostelFeewaveOff: 1,
            totalHostelFeePaid: 1,
            hostelFeePaid: 1,
            hostelFeepending: 1,
            hostelExpenses: 1,
            year: 1,
            comments: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of StudentFee', () => {
        const returnedFromService = Object.assign(
          {
            totalAcademicFee: 1,
            academicFeewaveOff: 1,
            academicFeePaid: 1,
            totalAcademicFeePaid: 1,
            academicFeepending: 1,
            busAlloted: true,
            hostelAlloted: true,
            totalBusFee: 1,
            busFeewaveOff: 1,
            busFeePaid: 1,
            totalBusFeePaid: 1,
            busFeepending: 1,
            totalHostelFee: 1,
            hostelFeewaveOff: 1,
            totalHostelFeePaid: 1,
            hostelFeePaid: 1,
            hostelFeepending: 1,
            hostelExpenses: 1,
            year: 1,
            comments: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a StudentFee', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
