import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { StudentMarkesDetailComponent } from 'app/entities/student-markes/student-markes-detail.component';
import { StudentMarkes } from 'app/shared/model/student-markes.model';

describe('Component Tests', () => {
  describe('StudentMarkes Management Detail Component', () => {
    let comp: StudentMarkesDetailComponent;
    let fixture: ComponentFixture<StudentMarkesDetailComponent>;
    const route = ({ data: of({ studentMarkes: new StudentMarkes(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [StudentMarkesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StudentMarkesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentMarkesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load studentMarkes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentMarkes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
