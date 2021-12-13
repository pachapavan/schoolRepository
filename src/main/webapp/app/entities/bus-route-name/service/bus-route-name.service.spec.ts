import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBusRouteName, BusRouteName } from '../bus-route-name.model';

import { BusRouteNameService } from './bus-route-name.service';

describe('BusRouteName Service', () => {
  let service: BusRouteNameService;
  let httpMock: HttpTestingController;
  let elemDefault: IBusRouteName;
  let expectedResult: IBusRouteName | IBusRouteName[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BusRouteNameService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      routeName: 'AAAAAAA',
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

    it('should create a BusRouteName', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BusRouteName()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BusRouteName', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          routeName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BusRouteName', () => {
      const patchObject = Object.assign({}, new BusRouteName());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BusRouteName', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          routeName: 'BBBBBB',
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

    it('should delete a BusRouteName', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBusRouteNameToCollectionIfMissing', () => {
      it('should add a BusRouteName to an empty array', () => {
        const busRouteName: IBusRouteName = { id: 123 };
        expectedResult = service.addBusRouteNameToCollectionIfMissing([], busRouteName);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(busRouteName);
      });

      it('should not add a BusRouteName to an array that contains it', () => {
        const busRouteName: IBusRouteName = { id: 123 };
        const busRouteNameCollection: IBusRouteName[] = [
          {
            ...busRouteName,
          },
          { id: 456 },
        ];
        expectedResult = service.addBusRouteNameToCollectionIfMissing(busRouteNameCollection, busRouteName);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BusRouteName to an array that doesn't contain it", () => {
        const busRouteName: IBusRouteName = { id: 123 };
        const busRouteNameCollection: IBusRouteName[] = [{ id: 456 }];
        expectedResult = service.addBusRouteNameToCollectionIfMissing(busRouteNameCollection, busRouteName);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(busRouteName);
      });

      it('should add only unique BusRouteName to an array', () => {
        const busRouteNameArray: IBusRouteName[] = [{ id: 123 }, { id: 456 }, { id: 2999 }];
        const busRouteNameCollection: IBusRouteName[] = [{ id: 123 }];
        expectedResult = service.addBusRouteNameToCollectionIfMissing(busRouteNameCollection, ...busRouteNameArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const busRouteName: IBusRouteName = { id: 123 };
        const busRouteName2: IBusRouteName = { id: 456 };
        expectedResult = service.addBusRouteNameToCollectionIfMissing([], busRouteName, busRouteName2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(busRouteName);
        expect(expectedResult).toContain(busRouteName2);
      });

      it('should accept null and undefined values', () => {
        const busRouteName: IBusRouteName = { id: 123 };
        expectedResult = service.addBusRouteNameToCollectionIfMissing([], null, busRouteName, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(busRouteName);
      });

      it('should return initial array if no BusRouteName is added', () => {
        const busRouteNameCollection: IBusRouteName[] = [{ id: 123 }];
        expectedResult = service.addBusRouteNameToCollectionIfMissing(busRouteNameCollection, undefined, null);
        expect(expectedResult).toEqual(busRouteNameCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
