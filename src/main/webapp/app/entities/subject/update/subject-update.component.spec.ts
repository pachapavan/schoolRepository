jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SubjectService } from '../service/subject.service';
import { ISubject, Subject } from '../subject.model';
import { IStudentMarkes } from 'app/entities/student-markes/student-markes.model';
import { StudentMarkesService } from 'app/entities/student-markes/service/student-markes.service';

import { SubjectUpdateComponent } from './subject-update.component';

describe('Subject Management Update Component', () => {
  let comp: SubjectUpdateComponent;
  let fixture: ComponentFixture<SubjectUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let subjectService: SubjectService;
  let studentMarkesService: StudentMarkesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SubjectUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SubjectUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubjectUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    subjectService = TestBed.inject(SubjectService);
    studentMarkesService = TestBed.inject(StudentMarkesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call StudentMarkes query and add missing value', () => {
      const subject: ISubject = { id: 456 };
      const studentMarkes: IStudentMarkes = { id: 16530 };
      subject.studentMarkes = studentMarkes;

      const studentMarkesCollection: IStudentMarkes[] = [{ id: 68632 }];
      jest.spyOn(studentMarkesService, 'query').mockReturnValue(of(new HttpResponse({ body: studentMarkesCollection })));
      const additionalStudentMarkes = [studentMarkes];
      const expectedCollection: IStudentMarkes[] = [...additionalStudentMarkes, ...studentMarkesCollection];
      jest.spyOn(studentMarkesService, 'addStudentMarkesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ subject });
      comp.ngOnInit();

      expect(studentMarkesService.query).toHaveBeenCalled();
      expect(studentMarkesService.addStudentMarkesToCollectionIfMissing).toHaveBeenCalledWith(
        studentMarkesCollection,
        ...additionalStudentMarkes
      );
      expect(comp.studentMarkesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const subject: ISubject = { id: 456 };
      const studentMarkes: IStudentMarkes = { id: 96989 };
      subject.studentMarkes = studentMarkes;

      activatedRoute.data = of({ subject });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(subject));
      expect(comp.studentMarkesSharedCollection).toContain(studentMarkes);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Subject>>();
      const subject = { id: 123 };
      jest.spyOn(subjectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subject });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subject }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(subjectService.update).toHaveBeenCalledWith(subject);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Subject>>();
      const subject = new Subject();
      jest.spyOn(subjectService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subject });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subject }));
      saveSubject.complete();

      // THEN
      expect(subjectService.create).toHaveBeenCalledWith(subject);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Subject>>();
      const subject = { id: 123 };
      jest.spyOn(subjectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subject });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(subjectService.update).toHaveBeenCalledWith(subject);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackStudentMarkesById', () => {
      it('Should return tracked StudentMarkes primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStudentMarkesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
