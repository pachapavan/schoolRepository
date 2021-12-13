jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StudentMarkesService } from '../service/student-markes.service';
import { IStudentMarkes, StudentMarkes } from '../student-markes.model';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';

import { StudentMarkesUpdateComponent } from './student-markes-update.component';

describe('StudentMarkes Management Update Component', () => {
  let comp: StudentMarkesUpdateComponent;
  let fixture: ComponentFixture<StudentMarkesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let studentMarkesService: StudentMarkesService;
  let studentService: StudentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [StudentMarkesUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(StudentMarkesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StudentMarkesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    studentMarkesService = TestBed.inject(StudentMarkesService);
    studentService = TestBed.inject(StudentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Student query and add missing value', () => {
      const studentMarkes: IStudentMarkes = { id: 456 };
      const student: IStudent = { id: 52199 };
      studentMarkes.student = student;

      const studentCollection: IStudent[] = [{ id: 16480 }];
      jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
      const additionalStudents = [student];
      const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
      jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ studentMarkes });
      comp.ngOnInit();

      expect(studentService.query).toHaveBeenCalled();
      expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(studentCollection, ...additionalStudents);
      expect(comp.studentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const studentMarkes: IStudentMarkes = { id: 456 };
      const student: IStudent = { id: 91987 };
      studentMarkes.student = student;

      activatedRoute.data = of({ studentMarkes });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(studentMarkes));
      expect(comp.studentsSharedCollection).toContain(student);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StudentMarkes>>();
      const studentMarkes = { id: 123 };
      jest.spyOn(studentMarkesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentMarkes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: studentMarkes }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(studentMarkesService.update).toHaveBeenCalledWith(studentMarkes);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StudentMarkes>>();
      const studentMarkes = new StudentMarkes();
      jest.spyOn(studentMarkesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentMarkes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: studentMarkes }));
      saveSubject.complete();

      // THEN
      expect(studentMarkesService.create).toHaveBeenCalledWith(studentMarkes);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<StudentMarkes>>();
      const studentMarkes = { id: 123 };
      jest.spyOn(studentMarkesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ studentMarkes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(studentMarkesService.update).toHaveBeenCalledWith(studentMarkes);
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
