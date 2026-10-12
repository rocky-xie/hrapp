import axios from 'axios';

import { type IRecruitmentRequisitionVersion } from '@/shared/model/recruitment-requisition-version.model';
import buildPaginationQueryOpts from '@/shared/sort/sorts';

const baseApiUrl = 'api/recruitment-requisition-versions';

export default class RecruitmentRequisitionVersionService {
  find(id: number): Promise<IRecruitmentRequisitionVersion> {
    return new Promise<IRecruitmentRequisitionVersion>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  retrieve(paginationQuery?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}?${buildPaginationQueryOpts(paginationQuery)}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  create(entity: IRecruitmentRequisitionVersion): Promise<IRecruitmentRequisitionVersion> {
    return new Promise<IRecruitmentRequisitionVersion>((resolve, reject) => {
      axios
        .post(baseApiUrl, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  update(entity: IRecruitmentRequisitionVersion): Promise<IRecruitmentRequisitionVersion> {
    return new Promise<IRecruitmentRequisitionVersion>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  partialUpdate(entity: IRecruitmentRequisitionVersion): Promise<IRecruitmentRequisitionVersion> {
    return new Promise<IRecruitmentRequisitionVersion>((resolve, reject) => {
      axios
        .patch(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
