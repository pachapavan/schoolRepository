import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { StaffSalaryComponent } from 'app/entities/staff-salary/staff-salary.component';
import { StaffSalaryService } from 'app/entities/staff-salary/staff-salary.service';
import { StaffSalary } from 'app/shared/model/staff-salary.model';

describe('Component Tests', () => {
  describe('StaffSalary Management Component', () => {
    let comp: StaffSalaryComponent;
    let fixture: ComponentFixture<StaffSalaryComponent>;
    let service: StaffSalaryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [StaffSalaryComponent]
      })
        .overrideTemplate(StaffSalaryComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StaffSalaryComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StaffSalaryService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new StaffSalary(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.staffSalaries && comp.staffSalaries[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
