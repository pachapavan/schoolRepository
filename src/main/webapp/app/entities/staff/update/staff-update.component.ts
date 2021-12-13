import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IStaff, Staff } from '../staff.model';
import { StaffService } from '../service/staff.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-staff-update',
  templateUrl: './staff-update.component.html',
})
export class StaffUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    staffId: [],
    staffName: [],
    phoneNumber: [],
    address: [],
    photo: [],
    photoContentType: [],
    isTeachingStaff: [],
    status: [],
    salary: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected staffService: StaffService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staff }) => {
      this.updateForm(staff);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('jhipsterSampleApplicationApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const staff = this.createFromForm();
    if (staff.id !== undefined) {
      this.subscribeToSaveResponse(this.staffService.update(staff));
    } else {
      this.subscribeToSaveResponse(this.staffService.create(staff));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStaff>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(staff: IStaff): void {
    this.editForm.patchValue({
      id: staff.id,
      staffId: staff.staffId,
      staffName: staff.staffName,
      phoneNumber: staff.phoneNumber,
      address: staff.address,
      photo: staff.photo,
      photoContentType: staff.photoContentType,
      isTeachingStaff: staff.isTeachingStaff,
      status: staff.status,
      salary: staff.salary,
    });
  }

  protected createFromForm(): IStaff {
    return {
      ...new Staff(),
      id: this.editForm.get(['id'])!.value,
      staffId: this.editForm.get(['staffId'])!.value,
      staffName: this.editForm.get(['staffName'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      address: this.editForm.get(['address'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      isTeachingStaff: this.editForm.get(['isTeachingStaff'])!.value,
      status: this.editForm.get(['status'])!.value,
      salary: this.editForm.get(['salary'])!.value,
    };
  }
}
