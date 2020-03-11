import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { AttendenceComponent } from 'app/entities/attendence/attendence.component';
import { AttendenceService } from 'app/entities/attendence/attendence.service';
import { Attendence } from 'app/shared/model/attendence.model';

describe('Component Tests', () => {
  describe('Attendence Management Component', () => {
    let comp: AttendenceComponent;
    let fixture: ComponentFixture<AttendenceComponent>;
    let service: AttendenceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [AttendenceComponent]
      })
        .overrideTemplate(AttendenceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttendenceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AttendenceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Attendence(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.attendences && comp.attendences[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
