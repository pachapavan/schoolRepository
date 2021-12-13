import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttendence, getAttendenceIdentifier } from '../attendence.model';

export type EntityResponseType = HttpResponse<IAttendence>;
export type EntityArrayResponseType = HttpResponse<IAttendence[]>;

@Injectable({ providedIn: 'root' })
export class AttendenceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendences');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(attendence: IAttendence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendence);
    return this.http
      .post<IAttendence>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(attendence: IAttendence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendence);
    return this.http
      .put<IAttendence>(`${this.resourceUrl}/${getAttendenceIdentifier(attendence) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(attendence: IAttendence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendence);
    return this.http
      .patch<IAttendence>(`${this.resourceUrl}/${getAttendenceIdentifier(attendence) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAttendence>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAttendence[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAttendenceToCollectionIfMissing(
    attendenceCollection: IAttendence[],
    ...attendencesToCheck: (IAttendence | null | undefined)[]
  ): IAttendence[] {
    const attendences: IAttendence[] = attendencesToCheck.filter(isPresent);
    if (attendences.length > 0) {
      const attendenceCollectionIdentifiers = attendenceCollection.map(attendenceItem => getAttendenceIdentifier(attendenceItem)!);
      const attendencesToAdd = attendences.filter(attendenceItem => {
        const attendenceIdentifier = getAttendenceIdentifier(attendenceItem);
        if (attendenceIdentifier == null || attendenceCollectionIdentifiers.includes(attendenceIdentifier)) {
          return false;
        }
        attendenceCollectionIdentifiers.push(attendenceIdentifier);
        return true;
      });
      return [...attendencesToAdd, ...attendenceCollection];
    }
    return attendenceCollection;
  }

  protected convertDateFromClient(attendence: IAttendence): IAttendence {
    return Object.assign({}, attendence, {
      month: attendence.month?.isValid() ? attendence.month.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.month = res.body.month ? dayjs(res.body.month) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((attendence: IAttendence) => {
        attendence.month = attendence.month ? dayjs(attendence.month) : undefined;
      });
    }
    return res;
  }
}
