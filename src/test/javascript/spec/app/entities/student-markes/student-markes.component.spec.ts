import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { StudentMarkesComponent } from 'app/entities/student-markes/student-markes.component';
import { StudentMarkesService } from 'app/entities/student-markes/student-markes.service';
import { StudentMarkes } from 'app/shared/model/student-markes.model';

describe('Component Tests', () => {
  describe('StudentMarkes Management Component', () => {
    let comp: StudentMarkesComponent;
    let fixture: ComponentFixture<StudentMarkesComponent>;
    let service: StudentMarkesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [StudentMarkesComponent]
      })
        .overrideTemplate(StudentMarkesComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentMarkesComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StudentMarkesService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new StudentMarkes(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.studentMarkes && comp.studentMarkes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
