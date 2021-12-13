import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStudentFee, StudentFee } from '../student-fee.model';

import { StudentFeeService } from './student-fee.service';

describe('StudentFee Service', () => {
  let service: StudentFeeService;
  let httpMock: HttpTestingController;
  let elemDefault: IStudentFee;
  let expectedResult: IStudentFee | IStudentFee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StudentFeeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      totalAcademicFee: 0,
      academicFeewaveOff: 0,
      academicFeePaid: 0,
      totalAcademicFeePaid: 0,
      academicFeepending: 0,
      busAlloted: false,
      hostelAlloted: false,
      totalBusFee: 0,
      busFeewaveOff: 0,
      busFeePaid: 0,
      totalBusFeePaid: 0,
      busFeepending: 0,
      totalHostelFee: 0,
      hostelFeewaveOff: 0,
      totalHostelFeePaid: 0,
      hostelFeePaid: 0,
      hostelFeepending: 0,
      hostelExpenses: 0,
      year: 0,
      comments: 'AAAAAAA',
    };
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
          id: 0,
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
          id: 1,
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
          comments: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StudentFee', () => {
      const patchObject = Object.assign(
        {
          academicFeewaveOff: 1,
          totalAcademicFeePaid: 1,
          busAlloted: true,
          totalBusFee: 1,
          busFeePaid: 1,
          busFeepending: 1,
          totalHostelFeePaid: 1,
          year: 1,
          comments: 'BBBBBB',
        },
        new StudentFee()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StudentFee', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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
          comments: 'BBBBBB',
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

    describe('addStudentFeeToCollectionIfMissing', () => {
      it('should add a StudentFee to an empty array', () => {
        const studentFee: IStudentFee = { id: 123 };
        expectedResult = service.addStudentFeeToCollectionIfMissing([], studentFee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(studentFee);
      });

      it('should not add a StudentFee to an array that contains it', () => {
        const studentFee: IStudentFee = { id: 123 };
        const studentFeeCollection: IStudentFee[] = [
          {
            ...studentFee,
          },
          { id: 456 },
        ];
        expectedResult = service.addStudentFeeToCollectionIfMissing(studentFeeCollection, studentFee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StudentFee to an array that doesn't contain it", () => {
        const studentFee: IStudentFee = { id: 123 };
        const studentFeeCollection: IStudentFee[] = [{ id: 456 }];
        expectedResult = service.addStudentFeeToCollectionIfMissing(studentFeeCollection, studentFee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(studentFee);
      });

      it('should add only unique StudentFee to an array', () => {
        const studentFeeArray: IStudentFee[] = [{ id: 123 }, { id: 456 }, { id: 24213 }];
        const studentFeeCollection: IStudentFee[] = [{ id: 123 }];
        expectedResult = service.addStudentFeeToCollectionIfMissing(studentFeeCollection, ...studentFeeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const studentFee: IStudentFee = { id: 123 };
        const studentFee2: IStudentFee = { id: 456 };
        expectedResult = service.addStudentFeeToCollectionIfMissing([], studentFee, studentFee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(studentFee);
        expect(expectedResult).toContain(studentFee2);
      });

      it('should accept null and undefined values', () => {
        const studentFee: IStudentFee = { id: 123 };
        expectedResult = service.addStudentFeeToCollectionIfMissing([], null, studentFee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(studentFee);
      });

      it('should return initial array if no StudentFee is added', () => {
        const studentFeeCollection: IStudentFee[] = [{ id: 123 }];
        expectedResult = service.addStudentFeeToCollectionIfMissing(studentFeeCollection, undefined, null);
        expect(expectedResult).toEqual(studentFeeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
