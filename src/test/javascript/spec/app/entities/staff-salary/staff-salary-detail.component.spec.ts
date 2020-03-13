import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { StaffSalaryDetailComponent } from 'app/entities/staff-salary/staff-salary-detail.component';
import { StaffSalary } from 'app/shared/model/staff-salary.model';

describe('Component Tests', () => {
  describe('StaffSalary Management Detail Component', () => {
    let comp: StaffSalaryDetailComponent;
    let fixture: ComponentFixture<StaffSalaryDetailComponent>;
    const route = ({ data: of({ staffSalary: new StaffSalary(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [StaffSalaryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StaffSalaryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StaffSalaryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load staffSalary on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.staffSalary).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
