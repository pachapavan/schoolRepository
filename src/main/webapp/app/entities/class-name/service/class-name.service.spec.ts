import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClassName, ClassName } from '../class-name.model';

import { ClassNameService } from './class-name.service';

describe('ClassName Service', () => {
  let service: ClassNameService;
  let httpMock: HttpTestingController;
  let elemDefault: IClassName;
  let expectedResult: IClassName | IClassName[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClassNameService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      classNumber: 0,
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

    it('should create a ClassName', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ClassName()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ClassName', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          classNumber: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ClassName', () => {
      const patchObject = Object.assign({}, new ClassName());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ClassName', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          classNumber: 1,
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

    it('should delete a ClassName', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addClassNameToCollectionIfMissing', () => {
      it('should add a ClassName to an empty array', () => {
        const className: IClassName = { id: 123 };
        expectedResult = service.addClassNameToCollectionIfMissing([], className);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(className);
      });

      it('should not add a ClassName to an array that contains it', () => {
        const className: IClassName = { id: 123 };
        const classNameCollection: IClassName[] = [
          {
            ...className,
          },
          { id: 456 },
        ];
        expectedResult = service.addClassNameToCollectionIfMissing(classNameCollection, className);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ClassName to an array that doesn't contain it", () => {
        const className: IClassName = { id: 123 };
        const classNameCollection: IClassName[] = [{ id: 456 }];
        expectedResult = service.addClassNameToCollectionIfMissing(classNameCollection, className);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(className);
      });

      it('should add only unique ClassName to an array', () => {
        const classNameArray: IClassName[] = [{ id: 123 }, { id: 456 }, { id: 80339 }];
        const classNameCollection: IClassName[] = [{ id: 123 }];
        expectedResult = service.addClassNameToCollectionIfMissing(classNameCollection, ...classNameArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const className: IClassName = { id: 123 };
        const className2: IClassName = { id: 456 };
        expectedResult = service.addClassNameToCollectionIfMissing([], className, className2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(className);
        expect(expectedResult).toContain(className2);
      });

      it('should accept null and undefined values', () => {
        const className: IClassName = { id: 123 };
        expectedResult = service.addClassNameToCollectionIfMissing([], null, className, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(className);
      });

      it('should return initial array if no ClassName is added', () => {
        const classNameCollection: IClassName[] = [{ id: 123 }];
        expectedResult = service.addClassNameToCollectionIfMissing(classNameCollection, undefined, null);
        expect(expectedResult).toEqual(classNameCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
