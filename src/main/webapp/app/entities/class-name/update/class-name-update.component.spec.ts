jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassNameService } from '../service/class-name.service';
import { IClassName, ClassName } from '../class-name.model';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { IStudentMarkes } from 'app/entities/student-markes/student-markes.model';
import { StudentMarkesService } from 'app/entities/student-markes/service/student-markes.service';
import { IStaff } from 'app/entities/staff/staff.model';
import { StaffService } from 'app/entities/staff/service/staff.service';

import { ClassNameUpdateComponent } from './class-name-update.component';

describe('ClassName Management Update Component', () => {
  let comp: ClassNameUpdateComponent;
  let fixture: ComponentFixture<ClassNameUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classNameService: ClassNameService;
  let studentService: StudentService;
  let studentMarkesService: StudentMarkesService;
  let staffService: StaffService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ClassNameUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ClassNameUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassNameUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classNameService = TestBed.inject(ClassNameService);
    studentService = TestBed.inject(StudentService);
    studentMarkesService = TestBed.inject(StudentMarkesService);
    staffService = TestBed.inject(StaffService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Student query and add missing value', () => {
      const className: IClassName = { id: 456 };
      const student: IStudent = { id: 23022 };
      className.student = student;

      const studentCollection: IStudent[] = [{ id: 63673 }];
      jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
      const additionalStudents = [student];
      const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
      jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ className });
      comp.ngOnInit();

      expect(studentService.query).toHaveBeenCalled();
      expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(studentCollection, ...additionalStudents);
      expect(comp.studentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call StudentMarkes query and add missing value', () => {
      const className: IClassName = { id: 456 };
      const studentMarkes: IStudentMarkes = { id: 53086 };
      className.studentMarkes = studentMarkes;

      const studentMarkesCollection: IStudentMarkes[] = [{ id: 43317 }];
      jest.spyOn(studentMarkesService, 'query').mockReturnValue(of(new HttpResponse({ body: studentMarkesCollection })));
      const additionalStudentMarkes = [studentMarkes];
      const expectedCollection: IStudentMarkes[] = [...additionalStudentMarkes, ...studentMarkesCollection];
      jest.spyOn(studentMarkesService, 'addStudentMarkesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ className });
      comp.ngOnInit();

      expect(studentMarkesService.query).toHaveBeenCalled();
      expect(studentMarkesService.addStudentMarkesToCollectionIfMissing).toHaveBeenCalledWith(
        studentMarkesCollection,
        ...additionalStudentMarkes
      );
      expect(comp.studentMarkesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Staff query and add missing value', () => {
      const className: IClassName = { id: 456 };
      const staff: IStaff = { id: 63280 };
      className.staff = staff;

      const staffCollection: IStaff[] = [{ id: 14160 }];
      jest.spyOn(staffService, 'query').mockReturnValue(of(new HttpResponse({ body: staffCollection })));
      const additionalStaff = [staff];
      const expectedCollection: IStaff[] = [...additionalStaff, ...staffCollection];
      jest.spyOn(staffService, 'addStaffToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ className });
      comp.ngOnInit();

      expect(staffService.query).toHaveBeenCalled();
      expect(staffService.addStaffToCollectionIfMissing).toHaveBeenCalledWith(staffCollection, ...additionalStaff);
      expect(comp.staffSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const className: IClassName = { id: 456 };
      const student: IStudent = { id: 60358 };
      className.student = student;
      const studentMarkes: IStudentMarkes = { id: 72054 };
      className.studentMarkes = studentMarkes;
      const staff: IStaff = { id: 14198 };
      className.staff = staff;

      activatedRoute.data = of({ className });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(className));
      expect(comp.studentsSharedCollection).toContain(student);
      expect(comp.studentMarkesSharedCollection).toContain(studentMarkes);
      expect(comp.staffSharedCollection).toContain(staff);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassName>>();
      const className = { id: 123 };
      jest.spyOn(classNameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ className });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: className }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(classNameService.update).toHaveBeenCalledWith(className);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassName>>();
      const className = new ClassName();
      jest.spyOn(classNameService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ className });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: className }));
      saveSubject.complete();

      // THEN
      expect(classNameService.create).toHaveBeenCalledWith(className);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassName>>();
      const className = { id: 123 };
      jest.spyOn(classNameService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ className });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classNameService.update).toHaveBeenCalledWith(className);
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

    describe('trackStudentMarkesById', () => {
      it('Should return tracked StudentMarkes primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStudentMarkesById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackStaffById', () => {
      it('Should return tracked Staff primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStaffById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
