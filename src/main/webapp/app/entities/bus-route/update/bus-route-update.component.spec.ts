jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BusRouteService } from '../service/bus-route.service';
import { IBusRoute, BusRoute } from '../bus-route.model';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

import { BusRouteUpdateComponent } from './bus-route-update.component';

describe('BusRoute Management Update Component', () => {
  let comp: BusRouteUpdateComponent;
  let fixture: ComponentFixture<BusRouteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let busRouteService: BusRouteService;
  let studentService: StudentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BusRouteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(BusRouteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BusRouteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    busRouteService = TestBed.inject(BusRouteService);
    studentService = TestBed.inject(StudentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Student query and add missing value', () => {
      const busRoute: IBusRoute = { id: 456 };
      const student: IStudent = { id: 53323 };
      busRoute.student = student;

      const studentCollection: IStudent[] = [{ id: 14248 }];
      jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
      const additionalStudents = [student];
      const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
      jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ busRoute });
      comp.ngOnInit();

      expect(studentService.query).toHaveBeenCalled();
      expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(studentCollection, ...additionalStudents);
      expect(comp.studentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const busRoute: IBusRoute = { id: 456 };
      const student: IStudent = { id: 56597 };
      busRoute.student = student;

      activatedRoute.data = of({ busRoute });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(busRoute));
      expect(comp.studentsSharedCollection).toContain(student);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusRoute>>();
      const busRoute = { id: 123 };
      jest.spyOn(busRouteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ busRoute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: busRoute }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(busRouteService.update).toHaveBeenCalledWith(busRoute);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusRoute>>();
      const busRoute = new BusRoute();
      jest.spyOn(busRouteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ busRoute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: busRoute }));
      saveSubject.complete();

      // THEN
      expect(busRouteService.create).toHaveBeenCalledWith(busRoute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BusRoute>>();
      const busRoute = { id: 123 };
      jest.spyOn(busRouteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ busRoute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(busRouteService.update).toHaveBeenCalledWith(busRoute);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackStudentById', () => {
      it('Should return tracked Student primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStudentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
