import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAttendence } from 'app/shared/model/attendence.model';

type EntityResponseType = HttpResponse<IAttendence>;
type EntityArrayResponseType = HttpResponse<IAttendence[]>;

@Injectable({ providedIn: 'root' })
export class AttendenceService {
  public resourceUrl = SERVER_API_URL + 'api/attendences';

  constructor(protected http: HttpClient) {}

  create(attendence: IAttendence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendence);
    return this.http
      .post<IAttendence>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(attendence: IAttendence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendence);
    return this.http
      .put<IAttendence>(this.resourceUrl, copy, { observe: 'response' })
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

  protected convertDateFromClient(attendence: IAttendence): IAttendence {
    const copy: IAttendence = Object.assign({}, attendence, {
      month: attendence.month && attendence.month.isValid() ? attendence.month.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.month = res.body.month ? moment(res.body.month) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((attendence: IAttendence) => {
        attendence.month = attendence.month ? moment(attendence.month) : undefined;
      });
    }
    return res;
  }
}
