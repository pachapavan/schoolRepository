jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBusRoute, BusRoute } from '../bus-route.model';
import { BusRouteService } from '../service/bus-route.service';

import { BusRouteRoutingResolveService } from './bus-route-routing-resolve.service';

describe('BusRoute routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BusRouteRoutingResolveService;
  let service: BusRouteService;
  let resultBusRoute: IBusRoute | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(BusRouteRoutingResolveService);
    service = TestBed.inject(BusRouteService);
    resultBusRoute = undefined;
  });

  describe('resolve', () => {
    it('should return IBusRoute returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusRoute = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBusRoute).toEqual({ id: 123 });
    });

    it('should return new IBusRoute if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusRoute = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBusRoute).toEqual(new BusRoute());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BusRoute })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusRoute = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBusRoute).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
