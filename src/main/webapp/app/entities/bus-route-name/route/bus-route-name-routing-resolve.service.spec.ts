jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBusRouteName, BusRouteName } from '../bus-route-name.model';
import { BusRouteNameService } from '../service/bus-route-name.service';

import { BusRouteNameRoutingResolveService } from './bus-route-name-routing-resolve.service';

describe('BusRouteName routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BusRouteNameRoutingResolveService;
  let service: BusRouteNameService;
  let resultBusRouteName: IBusRouteName | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(BusRouteNameRoutingResolveService);
    service = TestBed.inject(BusRouteNameService);
    resultBusRouteName = undefined;
  });

  describe('resolve', () => {
    it('should return IBusRouteName returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusRouteName = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBusRouteName).toEqual({ id: 123 });
    });

    it('should return new IBusRouteName if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusRouteName = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBusRouteName).toEqual(new BusRouteName());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BusRouteName })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusRouteName = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBusRouteName).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
