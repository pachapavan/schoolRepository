import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStudentMarkes, StudentMarkes } from '../student-markes.model';

import { StudentMarkesService } from './student-markes.service';

describe('StudentMarkes Service', () => {
  let service: StudentMarkesService;
  let httpMock: HttpTestingController;
  let elemDefault: IStudentMarkes;
  let expectedResult: IStudentMarkes | IStudentMarkes[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StudentMarkesService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      examName: 'AAAAAAA',
      totalMarkes: 0,
      markes: 0,
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

    it('should create a StudentMarkes', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new StudentMarkes()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StudentMarkes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          examName: 'BBBBBB',
          totalMarkes: 1,
          markes: 1,
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

    it('should partial update a StudentMarkes', () => {
      const patchObject = Object.assign(
        {
          examName: 'BBBBBB',
          totalMarkes: 1,
          markes: 1,
        },
        new StudentMarkes()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StudentMarkes', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          examName: 'BBBBBB',
          totalMarkes: 1,
          markes: 1,
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

    it('should delete a StudentMarkes', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStudentMarkesToCollectionIfMissing', () => {
      it('should add a StudentMarkes to an empty array', () => {
        const studentMarkes: IStudentMarkes = { id: 123 };
        expectedResult = service.addStudentMarkesToCollectionIfMissing([], studentMarkes);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(studentMarkes);
      });

      it('should not add a StudentMarkes to an array that contains it', () => {
        const studentMarkes: IStudentMarkes = { id: 123 };
        const studentMarkesCollection: IStudentMarkes[] = [
          {
            ...studentMarkes,
          },
          { id: 456 },
        ];
        expectedResult = service.addStudentMarkesToCollectionIfMissing(studentMarkesCollection, studentMarkes);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StudentMarkes to an array that doesn't contain it", () => {
        const studentMarkes: IStudentMarkes = { id: 123 };
        const studentMarkesCollection: IStudentMarkes[] = [{ id: 456 }];
        expectedResult = service.addStudentMarkesToCollectionIfMissing(studentMarkesCollection, studentMarkes);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(studentMarkes);
      });

      it('should add only unique StudentMarkes to an array', () => {
        const studentMarkesArray: IStudentMarkes[] = [{ id: 123 }, { id: 456 }, { id: 73055 }];
        const studentMarkesCollection: IStudentMarkes[] = [{ id: 123 }];
        expectedResult = service.addStudentMarkesToCollectionIfMissing(studentMarkesCollection, ...studentMarkesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const studentMarkes: IStudentMarkes = { id: 123 };
        const studentMarkes2: IStudentMarkes = { id: 456 };
        expectedResult = service.addStudentMarkesToCollectionIfMissing([], studentMarkes, studentMarkes2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(studentMarkes);
        expect(expectedResult).toContain(studentMarkes2);
      });

      it('should accept null and undefined values', () => {
        const studentMarkes: IStudentMarkes = { id: 123 };
        expectedResult = service.addStudentMarkesToCollectionIfMissing([], null, studentMarkes, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(studentMarkes);
      });

      it('should return initial array if no StudentMarkes is added', () => {
        const studentMarkesCollection: IStudentMarkes[] = [{ id: 123 }];
        expectedResult = service.addStudentMarkesToCollectionIfMissing(studentMarkesCollection, undefined, null);
        expect(expectedResult).toEqual(studentMarkesCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
