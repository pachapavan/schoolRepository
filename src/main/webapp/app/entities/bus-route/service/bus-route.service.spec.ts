import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBusRoute, BusRoute } from '../bus-route.model';

import { BusRouteService } from './bus-route.service';

describe('BusRoute Service', () => {
  let service: BusRouteService;
  let httpMock: HttpTestingController;
  let elemDefault: IBusRoute;
  let expectedResult: IBusRoute | IBusRoute[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BusRouteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      routeName: 'AAAAAAA',
      routeDriver: 'AAAAAAA',
      busNumber: 0,
      year: 0,
      status: 'AAAAAAA',
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

    it('should create a BusRoute', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BusRoute()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BusRoute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          routeName: 'BBBBBB',
          routeDriver: 'BBBBBB',
          busNumber: 1,
          year: 1,
          status: 'BBBBBB',
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

    it('should partial update a BusRoute', () => {
      const patchObject = Object.assign(
        {
          routeName: 'BBBBBB',
          routeDriver: 'BBBBBB',
          busNumber: 1,
          year: 1,
          comments: 'BBBBBB',
        },
        new BusRoute()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BusRoute', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          routeName: 'BBBBBB',
          routeDriver: 'BBBBBB',
          busNumber: 1,
          year: 1,
          status: 'BBBBBB',
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

    it('should delete a BusRoute', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBusRouteToCollectionIfMissing', () => {
      it('should add a BusRoute to an empty array', () => {
        const busRoute: IBusRoute = { id: 123 };
        expectedResult = service.addBusRouteToCollectionIfMissing([], busRoute);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(busRoute);
      });

      it('should not add a BusRoute to an array that contains it', () => {
        const busRoute: IBusRoute = { id: 123 };
        const busRouteCollection: IBusRoute[] = [
          {
            ...busRoute,
          },
          { id: 456 },
        ];
        expectedResult = service.addBusRouteToCollectionIfMissing(busRouteCollection, busRoute);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BusRoute to an array that doesn't contain it", () => {
        const busRoute: IBusRoute = { id: 123 };
        const busRouteCollection: IBusRoute[] = [{ id: 456 }];
        expectedResult = service.addBusRouteToCollectionIfMissing(busRouteCollection, busRoute);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(busRoute);
      });

      it('should add only unique BusRoute to an array', () => {
        const busRouteArray: IBusRoute[] = [{ id: 123 }, { id: 456 }, { id: 85456 }];
        const busRouteCollection: IBusRoute[] = [{ id: 123 }];
        expectedResult = service.addBusRouteToCollectionIfMissing(busRouteCollection, ...busRouteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const busRoute: IBusRoute = { id: 123 };
        const busRoute2: IBusRoute = { id: 456 };
        expectedResult = service.addBusRouteToCollectionIfMissing([], busRoute, busRoute2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(busRoute);
        expect(expectedResult).toContain(busRoute2);
      });

      it('should accept null and undefined values', () => {
        const busRoute: IBusRoute = { id: 123 };
        expectedResult = service.addBusRouteToCollectionIfMissing([], null, busRoute, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(busRoute);
      });

      it('should return initial array if no BusRoute is added', () => {
        const busRouteCollection: IBusRoute[] = [{ id: 123 }];
        expectedResult = service.addBusRouteToCollectionIfMissing(busRouteCollection, undefined, null);
        expect(expectedResult).toEqual(busRouteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
