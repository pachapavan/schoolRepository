jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SectionService } from '../service/section.service';
import { ISection, Section } from '../section.model';
import { IClassName } from 'app/entities/class-name/class-name.model';
import { ClassNameService } from 'app/entities/class-name/service/class-name.service';

import { SectionUpdateComponent } from './section-update.component';

describe('Section Management Update Component', () => {
  let comp: SectionUpdateComponent;
  let fixture: ComponentFixture<SectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sectionService: SectionService;
  let classNameService: ClassNameService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SectionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sectionService = TestBed.inject(SectionService);
    classNameService = TestBed.inject(ClassNameService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ClassName query and add missing value', () => {
      const section: ISection = { id: 456 };
      const className: IClassName = { id: 42700 };
      section.className = className;

      const classNameCollection: IClassName[] = [{ id: 39240 }];
      jest.spyOn(classNameService, 'query').mockReturnValue(of(new HttpResponse({ body: classNameCollection })));
      const additionalClassNames = [className];
      const expectedCollection: IClassName[] = [...additionalClassNames, ...classNameCollection];
      jest.spyOn(classNameService, 'addClassNameToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ section });
      comp.ngOnInit();

      expect(classNameService.query).toHaveBeenCalled();
      expect(classNameService.addClassNameToCollectionIfMissing).toHaveBeenCalledWith(classNameCollection, ...additionalClassNames);
      expect(comp.classNamesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const section: ISection = { id: 456 };
      const className: IClassName = { id: 17143 };
      section.className = className;

      activatedRoute.data = of({ section });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(section));
      expect(comp.classNamesSharedCollection).toContain(className);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Section>>();
      const section = { id: 123 };
      jest.spyOn(sectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: section }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sectionService.update).toHaveBeenCalledWith(section);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Section>>();
      const section = new Section();
      jest.spyOn(sectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: section }));
      saveSubject.complete();

      // THEN
      expect(sectionService.create).toHaveBeenCalledWith(section);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Section>>();
      const section = { id: 123 };
      jest.spyOn(sectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sectionService.update).toHaveBeenCalledWith(section);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClassNameById', () => {
      it('Should return tracked ClassName primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClassNameById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
