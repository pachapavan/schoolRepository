import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StaffSalaryDetailComponent } from './staff-salary-detail.component';

describe('StaffSalary Management Detail Component', () => {
  let comp: StaffSalaryDetailComponent;
  let fixture: ComponentFixture<StaffSalaryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StaffSalaryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ staffSalary: { id: 123 } }) },
        },
      ],
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
      expect(comp.staffSalary).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
