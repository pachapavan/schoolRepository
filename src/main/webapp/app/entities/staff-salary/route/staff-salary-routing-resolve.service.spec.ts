jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStaffSalary, StaffSalary } from '../staff-salary.model';
import { StaffSalaryService } from '../service/staff-salary.service';

import { StaffSalaryRoutingResolveService } from './staff-salary-routing-resolve.service';

describe('StaffSalary routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: StaffSalaryRoutingResolveService;
  let service: StaffSalaryService;
  let resultStaffSalary: IStaffSalary | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(StaffSalaryRoutingResolveService);
    service = TestBed.inject(StaffSalaryService);
    resultStaffSalary = undefined;
  });

  describe('resolve', () => {
    it('should return IStaffSalary returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStaffSalary = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStaffSalary).toEqual({ id: 123 });
    });

    it('should return new IStaffSalary if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStaffSalary = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultStaffSalary).toEqual(new StaffSalary());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as StaffSalary })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStaffSalary = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStaffSalary).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
