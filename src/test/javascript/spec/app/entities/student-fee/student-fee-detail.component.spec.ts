import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { StudentFeeDetailComponent } from 'app/entities/student-fee/student-fee-detail.component';
import { StudentFee } from 'app/shared/model/student-fee.model';

describe('Component Tests', () => {
  describe('StudentFee Management Detail Component', () => {
    let comp: StudentFeeDetailComponent;
    let fixture: ComponentFixture<StudentFeeDetailComponent>;
    const route = ({ data: of({ studentFee: new StudentFee(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [StudentFeeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StudentFeeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentFeeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load studentFee on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentFee).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
