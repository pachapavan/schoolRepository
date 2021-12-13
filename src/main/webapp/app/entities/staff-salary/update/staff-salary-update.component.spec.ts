jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StaffSalaryService } from '../service/staff-salary.service';
import { IStaffSalary, StaffSalary } from '../staff-salary.model';
import { IStaff } from 'app/entities/staff/staff.model';
import { StaffService } from 'app/entities/staff/service/staff.service';

import { StaffSalaryUpdateComponent } from './staff-salary-update.component';

describe('StaffSalary Management Update Component', () => {
  let comp: StaffSalaryUpdateComponent;
  let fixture: ComponentFixture<StaffSalaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let staffSalaryService: StaffSalaryService;
  let staffService: StaffService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StaffSalaryUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(StaffSalaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StaffSalaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    staffSalaryService = TestBed.inject(StaffSalaryService);
    staffService = TestBed.inject(StaffService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Staff query and add missing value', () => {
      const staffSalary: IStaffSalary = { id: 456 };
      const staff: IStaff = { id: 46154 };
      staffSalary.staff = staff;

      const staffCollection: IStaff[] = [{ id: 2430 }];
      jest.spyOn(staffService, 'query').mockReturnValue(of(new HttpResponse({ body: staffCollection })));
      const additionalStaff = [staff];
      const expectedCollection: IStaff[] = [...additionalStaff, ...staffCollection];
      jest.spyOn(staffService, 'addStaffToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ staffSalary });
      comp.ngOnInit();

      expect(staffService.query).toHaveBeenCalled();
      expect(staffService.addStaffToCollectionIfMissing).toHaveBeenCalledWith(staffCollection, ...additionalStaff);
      expect(comp.staffSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const staffSalary: IStaffSalary = { id: 456 };
      const staff: IStaff = { id: 18027 };
      staffSalary.staff = staff;

      activatedRoute.data = of({ staffSalary });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(staffSalary));
      expect(comp.staffSharedCollection).toContain(staff);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StaffSalary>>();
      const staffSalary = { id: 123 };
      jest.spyOn(staffSalaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staffSalary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: staffSalary }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(staffSalaryService.update).toHaveBeenCalledWith(staffSalary);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StaffSalary>>();
      const staffSalary = new StaffSalary();
      jest.spyOn(staffSalaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staffSalary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: staffSalary }));
      saveSubject.complete();

      // THEN
      expect(staffSalaryService.create).toHaveBeenCalledWith(staffSalary);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StaffSalary>>();
      const staffSalary = { id: 123 };
      jest.spyOn(staffSalaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ staffSalary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(staffSalaryService.update).toHaveBeenCalledWith(staffSalary);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackStaffById', () => {
      it('Should return tracked Staff primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStaffById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
