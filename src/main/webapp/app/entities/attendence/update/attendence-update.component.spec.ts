jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AttendenceService } from '../service/attendence.service';
import { IAttendence, Attendence } from '../attendence.model';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

import { AttendenceUpdateComponent } from './attendence-update.component';

describe('Attendence Management Update Component', () => {
  let comp: AttendenceUpdateComponent;
  let fixture: ComponentFixture<AttendenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let attendenceService: AttendenceService;
  let studentService: StudentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AttendenceUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AttendenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttendenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    attendenceService = TestBed.inject(AttendenceService);
    studentService = TestBed.inject(StudentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Student query and add missing value', () => {
      const attendence: IAttendence = { id: 456 };
      const student: IStudent = { id: 38452 };
      attendence.student = student;

      const studentCollection: IStudent[] = [{ id: 79026 }];
      jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
      const additionalStudents = [student];
      const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
      jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ attendence });
      comp.ngOnInit();

      expect(studentService.query).toHaveBeenCalled();
      expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(studentCollection, ...additionalStudents);
      expect(comp.studentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const attendence: IAttendence = { id: 456 };
      const student: IStudent = { id: 41980 };
      attendence.student = student;

      activatedRoute.data = of({ attendence });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(attendence));
      expect(comp.studentsSharedCollection).toContain(student);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Attendence>>();
      const attendence = { id: 123 };
      jest.spyOn(attendenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendence }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(attendenceService.update).toHaveBeenCalledWith(attendence);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Attendence>>();
      const attendence = new Attendence();
      jest.spyOn(attendenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attendence }));
      saveSubject.complete();

      // THEN
      expect(attendenceService.create).toHaveBeenCalledWith(attendence);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Attendence>>();
      const attendence = { id: 123 };
      jest.spyOn(attendenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attendence });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(attendenceService.update).toHaveBeenCalledWith(attendence);
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
